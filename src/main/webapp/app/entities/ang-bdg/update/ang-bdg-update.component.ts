import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IAngBdg, AngBdg } from '../ang-bdg.model';
import { AngBdgService } from '../service/ang-bdg.service';

@Component({
  selector: 'jhi-ang-bdg-update',
  templateUrl: './ang-bdg-update.component.html',
})
export class AngBdgUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    uid: [],
    bdgCod: [null, [Validators.required]],
  });

  constructor(protected angBdgService: AngBdgService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ angBdg }) => {
      this.updateForm(angBdg);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const angBdg = this.createFromForm();
    if (angBdg.id !== undefined) {
      this.subscribeToSaveResponse(this.angBdgService.update(angBdg));
    } else {
      this.subscribeToSaveResponse(this.angBdgService.create(angBdg));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAngBdg>>): void {
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

  protected updateForm(angBdg: IAngBdg): void {
    this.editForm.patchValue({
      id: angBdg.id,
      uid: angBdg.uid,
      bdgCod: angBdg.bdgCod,
    });
  }

  protected createFromForm(): IAngBdg {
    return {
      ...new AngBdg(),
      id: this.editForm.get(['id'])!.value,
      uid: this.editForm.get(['uid'])!.value,
      bdgCod: this.editForm.get(['bdgCod'])!.value,
    };
  }
}
