import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IAngGrp, AngGrp } from '../ang-grp.model';
import { AngGrpService } from '../service/ang-grp.service';

import { AngGrpRoutingResolveService } from './ang-grp-routing-resolve.service';

describe('AngGrp routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AngGrpRoutingResolveService;
  let service: AngGrpService;
  let resultAngGrp: IAngGrp | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(AngGrpRoutingResolveService);
    service = TestBed.inject(AngGrpService);
    resultAngGrp = undefined;
  });

  describe('resolve', () => {
    it('should return IAngGrp returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAngGrp = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAngGrp).toEqual({ id: 123 });
    });

    it('should return new IAngGrp if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAngGrp = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAngGrp).toEqual(new AngGrp());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AngGrp })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAngGrp = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAngGrp).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
