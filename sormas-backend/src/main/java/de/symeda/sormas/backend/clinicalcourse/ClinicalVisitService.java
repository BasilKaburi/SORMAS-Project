package de.symeda.sormas.backend.clinicalcourse;

import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.symeda.sormas.api.clinicalcourse.ClinicalVisitCriteria;
import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.caze.CaseService;
import de.symeda.sormas.backend.common.AbstractAdoService;
import de.symeda.sormas.backend.common.AbstractDomainObject;
import de.symeda.sormas.backend.symptoms.Symptoms;
import de.symeda.sormas.backend.user.User;

@Stateless
@LocalBean
public class ClinicalVisitService extends AbstractAdoService<ClinicalVisit> {

	@EJB
	CaseService caseService;
	
	public ClinicalVisitService() {
		super(ClinicalVisit.class);
	}
	
	public List<ClinicalVisit> findBy(ClinicalVisitCriteria criteria) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ClinicalVisit> cq = cb.createQuery(getElementClass());
		Root<ClinicalVisit> from = cq.from(getElementClass());
		
		Predicate filter = buildCriteriaFilter(criteria, cb, from);
		
		if (filter != null) {
			cq.where(filter);
		}
		cq.orderBy(cb.asc(from.get(ClinicalVisit.CREATION_DATE)));
		
		List<ClinicalVisit> resultList = em.createQuery(cq).getResultList();
		return resultList;
	}
	
	public List<ClinicalVisit> getAllActiveClinicalVisitsAfter(Date date, User user) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ClinicalVisit> cq = cb.createQuery(getElementClass());
		Root<ClinicalVisit> from = cq.from(getElementClass());
		Join<ClinicalVisit, ClinicalCourse> clinicalCourse = from.join(ClinicalVisit.CLINICAL_COURSE, JoinType.LEFT);
		Join<ClinicalCourse, Case> caze = clinicalCourse.join(ClinicalCourse.CASE, JoinType.LEFT);
	
		Predicate filter = cb.or(
				cb.equal(caze.get(Case.ARCHIVED), false),
				cb.isNull(caze.get(Case.ARCHIVED)));
		
		if (user != null) {
			Predicate userFilter = createUserFilter(cb, cq, from, user);
			filter = cb.and(filter, userFilter);
		}
		
		if (date != null) {
			Predicate dateFilter = createChangeDateFilter(cb, from, date);
			filter = cb.and(filter, dateFilter);
		}
		
		cq.where(filter);
		cq.orderBy(cb.desc(from.get(ClinicalVisit.CHANGE_DATE)));
		cq.distinct(true);
		
		return em.createQuery(cq).getResultList();
	}
	
	public List<String> getAllActiveUuids(User user) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<String> cq = cb.createQuery(String.class);
		Root<ClinicalVisit> from = cq.from(getElementClass());
		Join<ClinicalVisit, ClinicalCourse> clinicalCourse = from.join(ClinicalVisit.CLINICAL_COURSE, JoinType.LEFT);
		Join<ClinicalCourse, Case> caze = clinicalCourse.join(ClinicalCourse.CASE, JoinType.LEFT);
		
		Predicate filter = cb.or(
				cb.equal(caze.get(Case.ARCHIVED), false),
				cb.isNull(caze.get(Case.ARCHIVED)));
		
		if (user != null) {
			Predicate userFilter = createUserFilter(cb, cq, from, user);
			filter = cb.and(filter, userFilter);
		}
		
		cq.where(filter);
		cq.select(from.get(ClinicalVisit.UUID));
		
		return em.createQuery(cq).getResultList();
	}
	
	public Predicate buildCriteriaFilter(ClinicalVisitCriteria criteria, CriteriaBuilder cb, Root<ClinicalVisit> visit) {
		Predicate filter = null;
		Join<ClinicalVisit, ClinicalCourse> clinicalCourse = visit.join(ClinicalVisit.CLINICAL_COURSE, JoinType.LEFT);
		
		if (criteria.getClinicalCourse() != null) {
			filter = and(cb, filter, cb.equal(clinicalCourse.get(ClinicalCourse.UUID), criteria.getClinicalCourse().getUuid()));
		}
		
		return filter;
	}
	
	@Override
	public Predicate createChangeDateFilter(CriteriaBuilder cb, From<ClinicalVisit, ClinicalVisit> path, Date date) {
		Predicate dateFilter = cb.greaterThan(path.get(ClinicalVisit.CHANGE_DATE), date);
		
		Join<ClinicalVisit, Symptoms> symptoms = path.join(ClinicalVisit.SYMPTOMS, JoinType.LEFT);
		dateFilter = cb.or(dateFilter, cb.greaterThan(symptoms.get(AbstractDomainObject.CHANGE_DATE), date));
		
		return dateFilter;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Predicate createUserFilter(CriteriaBuilder cb, CriteriaQuery cq, From<ClinicalVisit, ClinicalVisit> from, User user) {
		Join<ClinicalVisit, ClinicalCourse> clinicalCourse = from.join(ClinicalVisit.CLINICAL_COURSE, JoinType.LEFT);
		return caseService.createUserFilter(cb, cq, clinicalCourse.join(ClinicalCourse.CASE, JoinType.LEFT), user);
	}
	
}