import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'ang-bdg',
        data: { pageTitle: 'jhipsterTest001App.angBdg.home.title' },
        loadChildren: () => import('./ang-bdg/ang-bdg.module').then(m => m.AngBdgModule),
      },
      {
        path: 'ang-res',
        data: { pageTitle: 'jhipsterTest001App.angRes.home.title' },
        loadChildren: () => import('./ang-res/ang-res.module').then(m => m.AngResModule),
      },
      {
        path: 'ang-grp',
        data: { pageTitle: 'jhipsterTest001App.angGrp.home.title' },
        loadChildren: () => import('./ang-grp/ang-grp.module').then(m => m.AngGrpModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
