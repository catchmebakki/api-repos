package com.ssi.ms.masslayoff.service;


import com.ssi.ms.masslayoff.database.repository.msl.MslReferenceListMlrlRepository;
import com.ssi.ms.masslayoff.dto.lookup.LookupListResDTO;
import com.ssi.ms.masslayoff.dto.lookup.LookupReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.ssi.ms.masslayoff.constant.PaginationAndSortByConstant.GET_DEFAULT_PAGINATION;
import static com.ssi.ms.masslayoff.constant.PaginationAndSortByConstant.GET_DEFAULT_SORTBY;
import static com.ssi.ms.masslayoff.constant.PaginationAndSortByConstant.MASSLAYOFF_LOOKUP_SORTBY_FIELDMAPPING;


/**
 * @author Praveenraja Paramsivam
 * LookUpService provides service to look list of msl reference data.
 */
@Slf4j
@Service
public class LookUpService {

    @Autowired
    private MslReferenceListMlrlRepository mslReferenceListMlrlRepository;

    /**
     * Retrieve search results based on the provided LookupReqDTO.
     *
     * @param lookupReqDTO {@link LookupReqDTO} The LookupReqDTO containing criteria for retrieving search results.
     * @return {@link LookupListResDTO} The LookupListResDTO containing the response for the search operation.
     */
    public LookupListResDTO getSearchResult(LookupReqDTO lookupReqDTO) {
        if (null == lookupReqDTO.getPagination()) {
            lookupReqDTO = lookupReqDTO.withPagination(GET_DEFAULT_PAGINATION.get());
        }
        if (null == lookupReqDTO.getSortBy()) {
            lookupReqDTO = lookupReqDTO.withSortBy(GET_DEFAULT_SORTBY.apply(MASSLAYOFF_LOOKUP_SORTBY_FIELDMAPPING));
        }
        return mslReferenceListMlrlRepository.filterMslReferenceListBasedLookupCriteria(lookupReqDTO);
    }
}
