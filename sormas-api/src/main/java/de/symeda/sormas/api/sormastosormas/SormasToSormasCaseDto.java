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

package de.symeda.sormas.api.sormastosormas;

import java.io.Serializable;
import java.util.List;

import de.symeda.sormas.api.caze.CaseDataDto;
import de.symeda.sormas.api.contact.ContactDto;
import de.symeda.sormas.api.person.PersonDto;

public class SormasToSormasCaseDto implements Serializable {

	private static final long serialVersionUID = 1811907980150876134L;

	private PersonDto person;

	private CaseDataDto caze;

	private List<AssociatedContactDto> associatedContacts;

	private SormasToSormasOriginInfoDto originInfo;

	public SormasToSormasCaseDto() {
	}

	public SormasToSormasCaseDto(PersonDto person, CaseDataDto caze, SormasToSormasOriginInfoDto originInfo) {
		this.person = person;
		this.caze = caze;
		this.originInfo = originInfo;
	}

	public PersonDto getPerson() {
		return person;
	}

	public CaseDataDto getCaze() {
		return caze;
	}

	public List<AssociatedContactDto> getAssociatedContacts() {
		return associatedContacts;
	}

	public void setAssociatedContacts(List<AssociatedContactDto> associatedContacts) {
		this.associatedContacts = associatedContacts;
	}

	public SormasToSormasOriginInfoDto getOriginInfo() {
		return originInfo;
	}

	public void setOriginInfo(SormasToSormasOriginInfoDto originInfo) {
		this.originInfo = originInfo;
	}

	public static final class AssociatedContactDto implements Serializable {

		private static final long serialVersionUID = 1398270981748143566L;

		private PersonDto person;

		private ContactDto contact;

		public AssociatedContactDto() {
		}

		public AssociatedContactDto(PersonDto person, ContactDto contact) {
			this.person = person;
			this.contact = contact;
		}

		public PersonDto getPerson() {
			return person;
		}

		public void setPerson(PersonDto person) {
			this.person = person;
		}

		public ContactDto getContact() {
			return contact;
		}

		public void setContact(ContactDto contact) {
			this.contact = contact;
		}
	}
}
