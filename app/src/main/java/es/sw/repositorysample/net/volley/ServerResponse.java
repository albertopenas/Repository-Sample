package es.sw.repositorysample.net.volley;

public interface  ServerResponse<T, J> {
    boolean wasCompletedSuccessfully();
    T getSuccessData();
    J getNotSuccessData();
}