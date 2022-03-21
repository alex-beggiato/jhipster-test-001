import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAngRes } from '../ang-res.model';
import { AngResService } from '../service/ang-res.service';

@Component({
  templateUrl: './ang-res-delete-dialog.component.html',
})
export class AngResDeleteDialogComponent {
  angRes?: IAngRes;

  constructor(protected angResService: AngResService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.angResService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
