import { type IUser } from '@/shared/model/user.model';

export interface IAppUser {
  id?: number;
  tenantId?: string | null;
  internalUser?: IUser | null;
}

export class AppUser implements IAppUser {
  constructor(
    public id?: number,
    public tenantId?: string | null,
    public internalUser?: IUser | null,
  ) {}
}
