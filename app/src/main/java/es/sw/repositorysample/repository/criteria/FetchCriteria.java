package es.sw.repositorysample.repository.criteria;

import es.sw.repositorysample.repository.exceptions.NoMoreCriteriaException;

/**
 * Created by albertopenasamor on 27/5/15.
 */
public class FetchCriteria {

    private FetchCriteriaEnum fetchCriteriaEnum;

    public FetchCriteria(FetchCriteriaEnum fetchCriteriaEnum) {
        this.fetchCriteriaEnum = fetchCriteriaEnum;
    }

    public FetchCriteriaEnum getFetchCriteria() {
        return fetchCriteriaEnum;
    }

    public boolean isNewData(){
        return fetchCriteriaEnum.isNewData();
    }

    public void next() throws NoMoreCriteriaException{
        fetchCriteriaEnum = fetchCriteriaEnum.next(fetchCriteriaEnum);
    }

    public enum FetchCriteriaEnum {
        /**
         * GET USED FOR FETCH FROM DB
         * REFRESH USED FOR FETCH FROM SERVER
         */
        GET(0), REFRESH(1);

        private int type;

        FetchCriteriaEnum(int type) {
            this.type = type;
        }

        public boolean isNewData(){
            return type == REFRESH.ordinal();
        }

        public FetchCriteriaEnum next(FetchCriteriaEnum fetchCriteria) throws NoMoreCriteriaException {
            if (fetchCriteria.type == GET.type){
                return FetchCriteriaEnum.REFRESH;
            }else {
                throw new NoMoreCriteriaException();
            }
        }
    }






}
