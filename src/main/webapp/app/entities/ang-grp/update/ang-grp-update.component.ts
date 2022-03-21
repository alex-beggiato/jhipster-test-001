import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAngGrp, AngGrp } from '../ang-grp.model';
import { AngGrpService } from '../service/ang-grp.service';

@Component({
  selector: 'jhi-ang-grp-update',
  templateUrl: './ang-grp-update.component.html',
})
export class AngGrpUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    uid: [],
    grpCod: [],
    grpDsc: [],
  });

  constructor(protected angGrpService: AngGrpService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ angGrp }) => {
      this.updateForm(angGrp);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const angGrp = this.createFromForm();
    if (angGrp.id !== undefined) {
      this.subscribeToSaveResponse(this.angGrpService.update(angGrp));
    } else {
      this.subscribeToSaveResponse(this.angGrpService.create(angGrp));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAngGrp>>): void {
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

  protected updateForm(angGrp: IAngGrp): void {
    this.editForm.patchValue({
      id: angGrp.id,
      uid: angGrp.uid,
      grpCod: angGrp.grpCod,
      grpDsc: angGrp.grpDsc,
    });
  }

  protected createFromForm(): IAngGrp {
    return {
      ...new AngGrp(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      grpCod: this.editForm.get(['grpCod'])!.value,
      grpDsc: this.editForm.get(['grpDsc'])!.value,
    };
  }
}
