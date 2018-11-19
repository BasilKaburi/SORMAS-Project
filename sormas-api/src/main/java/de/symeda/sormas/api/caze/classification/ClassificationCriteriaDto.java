package de.symeda.sormas.api.caze.classification;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.person.PersonDto;
import de.symeda.sormas.api.sample.SampleTestDto;

/**
 * Classification criteria are used to automatically classify cases and to be able to display
 * the underlying rules in the UI. A specific criteria can be evaluated using the eval method
 * which returns whether this criteria is applicable or not for the given case. A set of
 * criteria can be evaluated in order to determine whether the case should be classified as
 * suspect, probable, confirmed or not classified at all.
 */
@JsonTypeInfo(use=Id.NAME, include=As.PROPERTY)
@JsonSubTypes({
	@JsonSubTypes.Type(value = ClassificationAllOfCriteriaDto.class, name = "ClassificationAllOfCriteriaDto"),
	@JsonSubTypes.Type(value = ClassificationCaseCriteriaDto.class, name = "ClassificationCaseCriteriaDto"),
	@JsonSubTypes.Type(value = ClassificationNoneOfCriteriaDto.class, name = "ClassificationNoneOfCrieriaDto"),
	@JsonSubTypes.Type(value = ClassificationPersonAgeCriteriaDto.class, name = "ClassificationPersonAgeCriteriaDto"),
	@JsonSubTypes.Type(value = ClassificationSampleTestPositiveResultCriteriaDto.class, name = "ClassificationSampleTestPositiveResultCriteriaDto"),
	@JsonSubTypes.Type(value = ClassificationXOfCriteriaDto.class, name = "ClassificationXOfCriteriaDto"),
})
public abstract class ClassificationCriteriaDto implements Serializable {
	
	protected String type = getClass().getSimpleName(); 
	
	private static final long serialVersionUID = -3686569295881034008L;
	
	public abstract boolean eval(CaseDataDto caze, PersonDto person, List<SampleTestDto> sampleTests);
	public abstract String buildDescription();
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
}