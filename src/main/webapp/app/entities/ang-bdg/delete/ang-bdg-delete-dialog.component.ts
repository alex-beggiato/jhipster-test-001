import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAngBdg } from '../ang-bdg.model';
import { AngBdgService } from '../service/ang-bdg.service';

@Component({
  templateUrl: './ang-bdg-delete-dialog.component.html',
})
export class AngBdgDeleteDialogComponent {
  angBdg?: IAngBdg;

  constructor(protected angBdgService: AngBdgService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.angBdgService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
