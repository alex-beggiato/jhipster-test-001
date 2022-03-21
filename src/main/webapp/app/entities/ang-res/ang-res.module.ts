import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AngResComponent } from './list/ang-res.component';
import { AngResDetailComponent } from './detail/ang-res-detail.component';
import { AngResUpdateComponent } from './update/ang-res-update.component';
import { AngResDeleteDialogComponent } from './delete/ang-res-delete-dialog.component';
import { AngResRoutingModule } from './route/ang-res-routing.module';

@NgModule({
  imports: [SharedModule, AngResRoutingModule],
  declarations: [AngResComponent, AngResDetailComponent, AngResUpdateComponent, AngResDeleteDialogComponent],
  entryComponents: [AngResDeleteDialogComponent],
})
export class AngResModule {}
