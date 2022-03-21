import { IAngRes } from 'app/entities/ang-res/ang-res.model';

export interface IAngGrp {
  id?: number;
  uid?: string | null;
  grpCod?: string | null;
  grpDsc?: string | null;
  uids?: IAngRes[] | null;
}

export class AngGrp implements IAngGrp {
  constructor(
    public id?: number,
    public uid?: string | null,
    public grpCod?: string | null,
    public grpDsc?: string | null,
    public uids?: IAngRes[] | null
  ) {}
}

export function getAngGrpIdentifier(angGrp: IAngGrp): number | undefined {
  return angGrp.id;
}
