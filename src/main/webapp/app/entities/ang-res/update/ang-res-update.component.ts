import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAngRes, AngRes } from '../ang-res.model';
import { AngResService } from '../service/ang-res.service';
import { IAngBdg } from 'app/entities/ang-bdg/ang-bdg.model';
import { AngBdgService } from 'app/entities/ang-bdg/service/ang-bdg.service';
import { IAngGrp } from 'app/entities/ang-grp/ang-grp.model';
import { AngGrpService } from 'app/entities/ang-grp/service/ang-grp.service';
import { AngResTyp } from 'app/entities/enumerations/ang-res-typ.model';

@Component({
  selector: 'jhi-ang-res-update',
  templateUrl: './ang-res-update.component.html',
})
export class AngResUpdateComponent implements OnInit {
  isSaving = false;
  angResTypValues = Object.keys(AngResTyp);

  bdgUidsCollection: IAngBdg[] = [];
  angGrpsSharedCollection: IAngGrp[] = [];

  editForm = this.fb.group({
    id: [],
    uid: [],
    resCod: [],
    resDsc: [],
    bdgUid: [],
    resTyp: [],
    bdgUid: [],
    uids: [],
  });

  constructor(
    protected angResService: AngResService,
    protected angBdgService: AngBdgService,
    protected angGrpService: AngGrpService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ angRes }) => {
      this.updateForm(angRes);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const angRes = this.createFromForm();
    if (angRes.id !== undefined) {
      this.subscribeToSaveResponse(this.angResService.update(angRes));
    } else {
      this.subscribeToSaveResponse(this.angResService.create(angRes));
    }
  }

  trackAngBdgById(index: number, item: IAngBdg): number {
    return item.id!;
  }

  trackAngGrpById(index: number, item: IAngGrp): number {
    return item.id!;
  }

  getSelectedAngGrp(option: IAngGrp, selectedVals?: IAngGrp[]): IAngGrp {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAngRes>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(angRes: IAngRes): void {
    this.editForm.patchValue({
      id: angRes.id,
      uid: angRes.uid,
      resCod: angRes.resCod,
      resDsc: angRes.resDsc,
      bdgUid: angRes.bdgUid,
      resTyp: angRes.resTyp,
      bdgUid: angRes.bdgUid,
      uids: angRes.uids,
    });

    this.bdgUidsCollection = this.angBdgService.addAngBdgToCollectionIfMissing(this.bdgUidsCollection, angRes.bdgUid);
    this.angGrpsSharedCollection = this.angGrpService.addAngGrpToCollectionIfMissing(this.angGrpsSharedCollection, ...(angRes.uids ?? []));
  }

  protected loadRelationshipsOptions(): void {
    this.angBdgService
      .query({ filter: 'angres-is-null' })
      .pipe(map((res: HttpResponse<IAngBdg[]>) => res.body ?? []))
      .pipe(map((angBdgs: IAngBdg[]) => this.angBdgService.addAngBdgToCollectionIfMissing(angBdgs, this.editForm.get('bdgUid')!.value)))
      .subscribe((angBdgs: IAngBdg[]) => (this.bdgUidsCollection = angBdgs));

    this.angGrpService
      .query()
      .pipe(map((res: HttpResponse<IAngGrp[]>) => res.body ?? []))
      .pipe(
        map((angGrps: IAngGrp[]) => this.angGrpService.addAngGrpToCollectionIfMissing(angGrps, ...(this.editForm.get('uids')!.value ?? [])))
      )
      .subscribe((angGrps: IAngGrp[]) => (this.angGrpsSharedCollection = angGrps));
  }

  protected createFromForm(): IAngRes {
    return {
      ...new AngRes(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      resCod: this.editForm.get(['resCod'])!.value,
      resDsc: this.editForm.get(['resDsc'])!.value,
      bdgUid: this.editForm.get(['bdgUid'])!.value,
      resTyp: this.editForm.get(['resTyp'])!.value,
      bdgUid: this.editForm.get(['bdgUid'])!.value,
      uids: this.editForm.get(['uids'])!.value,
    };
  }
}
