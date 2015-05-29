package es.sw.repositorysample.repository.criteria;

import es.sw.repositorysample.repository.exceptions.NoMoreCriteriaException;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public enum FetchCriteria {
    /**
     * GET USED FOR FETCH FROM DB
     * REFRESH USED FOR FETCH FROM SERVER
     */
    GET(0), REFRESH(1);

    private int type;

    FetchCriteria(int type) {
        this.type = type;
    }

    public static FetchCriteria next(FetchCriteria fetchCriteria) throws NoMoreCriteriaException {
        if (fetchCriteria.type == GET.type){
            return REFRESH;
        }else {
            throw new NoMoreCriteriaException();
        }
    }
}
