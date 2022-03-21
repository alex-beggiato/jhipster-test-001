import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AngGrpService } from '../service/ang-grp.service';
import { IAngGrp, AngGrp } from '../ang-grp.model';

import { AngGrpUpdateComponent } from './ang-grp-update.component';

describe('AngGrp Management Update Component', () => {
  let comp: AngGrpUpdateComponent;
  let fixture: ComponentFixture<AngGrpUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let angGrpService: AngGrpService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AngGrpUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AngGrpUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AngGrpUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    angGrpService = TestBed.inject(AngGrpService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const angGrp: IAngGrp = { id: 456 };

      activatedRoute.data = of({ angGrp });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(angGrp));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AngGrp>>();
      const angGrp = { id: 123 };
      jest.spyOn(angGrpService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ angGrp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: angGrp }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(angGrpService.update).toHaveBeenCalledWith(angGrp);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AngGrp>>();
      const angGrp = new AngGrp();
      jest.spyOn(angGrpService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ angGrp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: angGrp }));
      saveSubject.complete();

      // THEN
      expect(angGrpService.create).toHaveBeenCalledWith(angGrp);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AngGrp>>();
      const angGrp = { id: 123 };
      jest.spyOn(angGrpService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ angGrp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(angGrpService.update).toHaveBeenCalledWith(angGrp);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
