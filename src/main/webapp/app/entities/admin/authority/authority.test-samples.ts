import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'a2bc16bb-2627-4089-a613-5cc613ccf856',
};

export const sampleWithPartialData: IAuthority = {
  name: 'b422db5a-40f7-4c1b-9374-e49dc0ba8c73',
};

export const sampleWithFullData: IAuthority = {
  name: 'e74401a4-447a-4cab-92f7-2cf1efccf200',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
