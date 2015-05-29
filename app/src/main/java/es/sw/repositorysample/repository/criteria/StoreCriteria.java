package es.sw.repositorysample.repository.criteria;

import es.sw.repositorysample.repository.exceptions.NoMoreCriteriaException;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public enum StoreCriteria {
    /**
     * SAVE USED FOR STORE IN DB
     */
    SAVE(0);

    private int type;

    StoreCriteria(int type) {
        this.type = type;
    }

    public static StoreCriteria next(StoreCriteria fetchCriteria) throws NoMoreCriteriaException {
        throw new NoMoreCriteriaException();
    }
}
