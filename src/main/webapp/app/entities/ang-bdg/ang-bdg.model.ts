export interface IAngBdg {
  id?: number;
  uid?: string | null;
  bdgCod?: string;
}

export class AngBdg implements IAngBdg {
  constructor(public id?: number, public uid?: string | null, public bdgCod?: string) {}
}

export function getAngBdgIdentifier(angBdg: IAngBdg): number | undefined {
  return angBdg.id;
}
