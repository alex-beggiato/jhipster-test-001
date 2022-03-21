import { IAngBdg } from 'app/entities/ang-bdg/ang-bdg.model';
import { IAngGrp } from 'app/entities/ang-grp/ang-grp.model';
import { AngResTyp } from 'app/entities/enumerations/ang-res-typ.model';

export interface IAngRes {
  id?: number;
  uid?: string | null;
  resCod?: string | null;
  resDsc?: string | null;
  bdgUid?: number | null;
  resTyp?: AngResTyp | null;
  bdgUid?: IAngBdg | null;
  uids?: IAngGrp[] | null;
}

export class AngRes implements IAngRes {
  constructor(
    public id?: number,
    public uid?: string | null,
    public resCod?: string | null,
    public resDsc?: string | null,
    public bdgUid?: number | null,
    public resTyp?: AngResTyp | null,
    public bdgUid?: IAngBdg | null,
    public uids?: IAngGrp[] | null
  ) {}
}

export function getAngResIdentifier(angRes: IAngRes): number | undefined {
  return angRes.id;
}
