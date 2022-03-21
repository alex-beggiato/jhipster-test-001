import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AngBdgComponent } from '../list/ang-bdg.component';
import { AngBdgDetailComponent } from '../detail/ang-bdg-detail.component';
import { AngBdgUpdateComponent } from '../update/ang-bdg-update.component';
import { AngBdgRoutingResolveService } from './ang-bdg-routing-resolve.service';

const angBdgRoute: Routes = [
  {
    path: '',
    component: AngBdgComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AngBdgDetailComponent,
    resolve: {
      angBdg: AngBdgRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AngBdgUpdateComponent,
    resolve: {
      angBdg: AngBdgRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AngBdgUpdateComponent,
    resolve: {
      angBdg: AngBdgRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(angBdgRoute)],
  exports: [RouterModule],
})
export class AngBdgRoutingModule {}
