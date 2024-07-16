export interface IAppStats {
  id?: number;
  usedTenantId?: string | null;
}

export class AppStats implements IAppStats {
  constructor(
    public id?: number,
    public usedTenantId?: string | null,
  ) {}
}
