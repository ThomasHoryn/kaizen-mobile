import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 11847,
  login: '5',
};

export const sampleWithPartialData: IUser = {
  id: 28035,
  login: 'yqzOk',
};

export const sampleWithFullData: IUser = {
  id: 26151,
  login: 'sh',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
