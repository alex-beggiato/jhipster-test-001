import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AngGrpComponent } from './list/ang-grp.component';
import { AngGrpDetailComponent } from './detail/ang-grp-detail.component';
import { AngGrpUpdateComponent } from './update/ang-grp-update.component';
import { AngGrpDeleteDialogComponent } from './delete/ang-grp-delete-dialog.component';
import { AngGrpRoutingModule } from './route/ang-grp-routing.module';

@NgModule({
  imports: [SharedModule, AngGrpRoutingModule],
  declarations: [AngGrpComponent, AngGrpDetailComponent, AngGrpUpdateComponent, AngGrpDeleteDialogComponent],
  entryComponents: [AngGrpDeleteDialogComponent],
})
export class AngGrpModule {}
