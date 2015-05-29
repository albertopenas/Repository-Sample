package es.sw.repositorysample.net.volley;

public interface ResponseListener<T, J> {
    void onSuccess(T successData);
    void onError(J errorResponse);
}
