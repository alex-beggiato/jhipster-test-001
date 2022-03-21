import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AngBdgService } from '../service/ang-bdg.service';
import { IAngBdg, AngBdg } from '../ang-bdg.model';

import { AngBdgUpdateComponent } from './ang-bdg-update.component';

describe('AngBdg Management Update Component', () => {
  let comp: AngBdgUpdateComponent;
  let fixture: ComponentFixture<AngBdgUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let angBdgService: AngBdgService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AngBdgUpdateComponent],
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
      .overrideTemplate(AngBdgUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AngBdgUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    angBdgService = TestBed.inject(AngBdgService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const angBdg: IAngBdg = { id: 456 };

      activatedRoute.data = of({ angBdg });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(angBdg));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AngBdg>>();
      const angBdg = { id: 123 };
      jest.spyOn(angBdgService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ angBdg });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: angBdg }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(angBdgService.update).toHaveBeenCalledWith(angBdg);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AngBdg>>();
      const angBdg = new AngBdg();
      jest.spyOn(angBdgService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ angBdg });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: angBdg }));
      saveSubject.complete();

      // THEN
      expect(angBdgService.create).toHaveBeenCalledWith(angBdg);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<AngBdg>>();
      const angBdg = { id: 123 };
      jest.spyOn(angBdgService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ angBdg });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(angBdgService.update).toHaveBeenCalledWith(angBdg);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
