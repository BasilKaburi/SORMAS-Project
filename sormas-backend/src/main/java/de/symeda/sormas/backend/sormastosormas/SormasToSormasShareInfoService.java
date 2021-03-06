/*
 * SORMAS® - Surveillance Outbreak Response Management & Analysis System
 * Copyright © 2016-2020 Helmholtz-Zentrum für Infektionsforschung GmbH (HZI)
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package de.symeda.sormas.backend.sormastosormas;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.From;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import de.symeda.sormas.api.sormastosormas.SormasToSormasShareInfoCriteria;
import de.symeda.sormas.backend.caze.Case;
import de.symeda.sormas.backend.common.AbstractAdoService;
import de.symeda.sormas.backend.contact.Contact;

@Stateless
@LocalBean
public class SormasToSormasShareInfoService extends AbstractAdoService<SormasToSormasShareInfo> {

	public SormasToSormasShareInfoService() {
		super(SormasToSormasShareInfo.class);
	}

	@Override
	public Predicate createUserFilter(CriteriaBuilder cb, CriteriaQuery cq, From<?, SormasToSormasShareInfo> from) {
		// no user filter needed right now
		return null;
	}

	public Predicate buildCriteriaFilter(SormasToSormasShareInfoCriteria criteria, CriteriaBuilder cb, Root<SormasToSormasShareInfo> from) {
		Predicate filter = null;

		if (criteria.getCaze() != null) {
			filter = and(cb, filter, cb.equal(from.join(SormasToSormasShareInfo.CAZE, JoinType.LEFT).get(Case.UUID), criteria.getCaze().getUuid()));
		}

		if (criteria.getContact() != null) {
			filter =
				and(cb, filter, cb.equal(from.join(SormasToSormasShareInfo.CONTACT, JoinType.LEFT).get(Case.UUID), criteria.getContact().getUuid()));
		}

		return filter;
	}

	public boolean isCaseOwnershipHandedOver(Case caze) {
		return exists(
			(cb, root) -> cb
				.and(cb.equal(root.get(SormasToSormasShareInfo.CAZE), caze), cb.isTrue(root.get(SormasToSormasShareInfo.OWNERSHIP_HANDED_OVER))));
	}

	public boolean isContactOwnershipHandedOver(Contact contact) {
		return exists(
			(cb, root) -> cb.and(
				cb.equal(root.get(SormasToSormasShareInfo.CONTACT), contact),
				cb.isTrue(root.get(SormasToSormasShareInfo.OWNERSHIP_HANDED_OVER))));
	}
}
