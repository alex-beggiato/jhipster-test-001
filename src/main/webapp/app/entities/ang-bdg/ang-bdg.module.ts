import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AngBdgComponent } from './list/ang-bdg.component';
import { AngBdgDetailComponent } from './detail/ang-bdg-detail.component';
import { AngBdgUpdateComponent } from './update/ang-bdg-update.component';
import { AngBdgDeleteDialogComponent } from './delete/ang-bdg-delete-dialog.component';
import { AngBdgRoutingModule } from './route/ang-bdg-routing.module';

@NgModule({
  imports: [SharedModule, AngBdgRoutingModule],
  declarations: [AngBdgComponent, AngBdgDetailComponent, AngBdgUpdateComponent, AngBdgDeleteDialogComponent],
  entryComponents: [AngBdgDeleteDialogComponent],
})
export class AngBdgModule {}
