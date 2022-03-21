import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAngGrp } from '../ang-grp.model';
import { AngGrpService } from '../service/ang-grp.service';

@Component({
  templateUrl: './ang-grp-delete-dialog.component.html',
})
export class AngGrpDeleteDialogComponent {
  angGrp?: IAngGrp;

  constructor(protected angGrpService: AngGrpService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.angGrpService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
