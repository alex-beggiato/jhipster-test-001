import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AngGrpComponent } from '../list/ang-grp.component';
import { AngGrpDetailComponent } from '../detail/ang-grp-detail.component';
import { AngGrpUpdateComponent } from '../update/ang-grp-update.component';
import { AngGrpRoutingResolveService } from './ang-grp-routing-resolve.service';

const angGrpRoute: Routes = [
  {
    path: '',
    component: AngGrpComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AngGrpDetailComponent,
    resolve: {
      angGrp: AngGrpRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AngGrpUpdateComponent,
    resolve: {
      angGrp: AngGrpRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AngGrpUpdateComponent,
    resolve: {
      angGrp: AngGrpRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(angGrpRoute)],
  exports: [RouterModule],
})
export class AngGrpRoutingModule {}
