package es.sw.repositorysample.database;

/**
 * Created by albertopenasamor on 26/5/15.
 */
public class BlockOperationException extends RuntimeException {
    public BlockOperationException() {
        super("Have you call obtainHelper in try-catch block before database operations. Ensure to call releaseHelper in finally block");
    }
}
