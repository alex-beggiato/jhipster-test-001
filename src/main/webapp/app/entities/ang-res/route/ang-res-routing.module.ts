import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AngResComponent } from '../list/ang-res.component';
import { AngResDetailComponent } from '../detail/ang-res-detail.component';
import { AngResUpdateComponent } from '../update/ang-res-update.component';
import { AngResRoutingResolveService } from './ang-res-routing-resolve.service';

const angResRoute: Routes = [
  {
    path: '',
    component: AngResComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AngResDetailComponent,
    resolve: {
      angRes: AngResRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AngResUpdateComponent,
    resolve: {
      angRes: AngResRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AngResUpdateComponent,
    resolve: {
      angRes: AngResRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(angResRoute)],
  exports: [RouterModule],
})
export class AngResRoutingModule {}
