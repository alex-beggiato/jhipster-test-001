import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AngBdgDetailComponent } from './ang-bdg-detail.component';

describe('AngBdg Management Detail Component', () => {
  let comp: AngBdgDetailComponent;
  let fixture: ComponentFixture<AngBdgDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AngBdgDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ angBdg: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AngBdgDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AngBdgDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load angBdg on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.angBdg).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
