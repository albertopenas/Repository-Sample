package es.sw.repositorysample.database;

/**
 * Created by albertopenasamor on 11/11/13.
 */

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import es.sw.repositorysample.domain.database.DatabaseHelper;

import static android.util.Log.d;
import static android.util.Log.e;

/**
 * TODO: NOT MULTI-THREAD IMPLEMENTATION
 */
public abstract class ActiveRecord extends Object{

    private static final String TAG = ActiveRecord.class.getSimpleName();

    public final static boolean DEBUG = false;

    //Compare
	public static final int EQ = 0;
	public static final int N_EQ = EQ + 1;
	public static final int GEQ = N_EQ + 1;
	public static final int LEQ = GEQ + 1;
	public static final int LT = LEQ + 1;
	public static final int GT = LT + 1;
    //clauses
	public static final int AND = 0;
	public static final int OR = AND +1;
    //order
	public static final String ORDER_ASC = "ASC";
	public static final String ORDER_DESC = "DESC";
    //helper
	private static DatabaseHelper db;


    /**
     * Obtain helper for normal records
     * @param context
     * @return
     */
    private static DatabaseHelper getHelper(Context context){
        if (DEBUG) {
            d(TAG, "getHelper");
        }

        return OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    /**
     * Obtain helper for bulk records
     * @param context
     * @return
     */
    public static DatabaseHelper obtainHelper(Context context){
        if (DEBUG) {
            d(TAG, "obtainHelper");
        }
        if (db == null){
            db = OpenHelperManager.getHelper(context, DatabaseHelper.class);
        }
        return db;
    }

    /**
     * Helper for get DAO
     * @param context
     * @param c
     * @return
     */
    private static Dao getDao(Context context, Class c){
        if (DEBUG) {
            d(TAG, "getDao");
        }
        DatabaseHelper db = getHelper(context);
        try {
            Dao<Class, Integer> dao = db.getDao(c);
            return dao;
        } catch (SQLException e) {
            throw new IllegalArgumentException("cannot get Dao for class: " + c.getSimpleName());
        }
    }

    /**
     * Returns concrete DAO for multiple ops
     * @param c
     * @return
     * @throws BlockOperationException if obtainHelper does not called before
     */
    private static Dao getCachedDao(Class c) throws BlockOperationException {
        if (DEBUG) {
            d(TAG, "getCachedDao");
        }
        if (db == null){
            throw new BlockOperationException();
        }
        try {
            Dao<Class, Integer> dao = db.getDao(c);
            return dao;
        } catch (SQLException e) {
            throw new IllegalArgumentException("cannot get Dao for class: " + c.getSimpleName());
        }
    }

    /**
     * Helper for release dao if is not perfirming a block operation
     */
    private static void releaseDao(){
        if (DEBUG) {
            d(TAG, "releaseDao");
        }
        OpenHelperManager.releaseHelper();
    }


    /**
     * Release helper for bulk records
     */
    public static void releaseHelper(){
        if (DEBUG) {
            d(TAG, "releaseDao");
        }
        OpenHelperManager.releaseHelper();
        db = null;
    }
    
    /**
     * Helper for query if a Dao.CreateOrUpdateStatus was created or updated
     * @param status
     * @return
     */
    private static boolean isCreatedOrUpdated(CreateOrUpdateStatus status){
        if (status == null) {
            return false;
        }

        if (status.isCreated() || status.isUpdated()) {
            return true;
        }

        return false;
    }

    public static<T extends ActiveRecord> List<T>findAll(Context ctx, Class table){
        Dao<T,Integer> dao = ActiveRecord.getDao(ctx, table);
        List<T> list = null;
        if (dao != null){
            try {
                list = dao.queryForAll();
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
            	e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }finally {
                releaseDao();
            }
        }
        if (list == null){
        	return new ArrayList<>();
        }
        return list;
    }

    public static<T extends ActiveRecord> T findById(Context ctx, Class table, Integer id){
        Dao<T,Integer> dao = ActiveRecord.getDao(ctx, table);
        T t = null;
        if (dao != null){
            try {
                t = dao.queryForId(id);
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
            	e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }finally {
                releaseDao();
            }
        }
        return t;
    }

    public static<T extends ActiveRecord> List<T> findByField(Context ctx, Class table, String field, Object value){
        Dao<T,Integer> dao = ActiveRecord.getDao(ctx, table);
        List<T> list = new ArrayList<>();
        if (dao != null){
            try {
                list = dao.queryForEq(field, value);
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
                e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }finally {
                releaseDao();
            }
        }
        return list;
    }
    
    public static<T extends ActiveRecord> List<T> orderBy(Context context, Class table, String orderColumn, long limit, boolean ascending){
    	Dao<T,Integer> dao = ActiveRecord.getDao(context, table);
        List<T> list = null;
        if (dao != null){
            try {
                list = dao.queryBuilder().orderBy(orderColumn, ascending).limit(limit).query();
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
            	e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }finally {
                releaseDao();
            }
        }
        if (list == null){
        	list = new ArrayList<T>();
        }
        return list;
    }

    public static<T extends ActiveRecord> boolean save(Context ctx, T t){
    	Class table = t.getClass();
        Dao<T,Integer> dao = ActiveRecord.getDao(ctx, table);
        boolean success = false;        
        if (dao != null){
            try {
                success = isCreatedOrUpdated(dao.createOrUpdate(t));
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
            	e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }finally {
                releaseDao();
            }
        }
        return success;
    }

    /**
     * save  current item. Only use with obtainHelper before and releasehelper after block of operations
     * @param t
     * @param <T>
     * @return
     */
    public static<T extends ActiveRecord> boolean save(T t){
        Class table = t.getClass();
        Dao<T,Integer> dao = ActiveRecord.getCachedDao(table);
        boolean success = false;
        if (dao != null){
            try {
                success = isCreatedOrUpdated(dao.createOrUpdate(t));
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
                e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }
        }
        return success;
    }
    
    public static<T extends ActiveRecord> CreateOrUpdateStatus saveOrUpdate(Context ctx, T t){
    	Class table = t.getClass();
        Dao<T,Integer> dao = ActiveRecord.getDao(ctx, table);
        if (dao != null){
            try {
            	CreateOrUpdateStatus state = dao.createOrUpdate(t);
            	return state;
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
            	e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }finally {
                releaseDao();
            }
        }
        return new CreateOrUpdateStatus(false, false, 0);
    }



    /**
     * save or update current item. Only use with obtainHelper before and releasehelper after block of operations
     * @param t
     * @param <T>
     * @return
     */
    public static<T extends ActiveRecord> CreateOrUpdateStatus saveOrUpdate(T t){
        Class table = t.getClass();
        Dao<T,Integer> dao = ActiveRecord.getCachedDao(table);
        if (dao != null){
            try {
                CreateOrUpdateStatus state = dao.createOrUpdate(t);
                return state;
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
                e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }
        }
        return new CreateOrUpdateStatus(false, false, 0);
    }

    public static<T extends ActiveRecord> boolean saveAll(Context ctx, final List<T> list){
        if (list.isEmpty()){
            if (DEBUG) {
                Log.d(TAG, "saveAllBulk nothing to save, list is empty");
            }
            return false;
        }
        Class table = list.get(0).getClass();
        final Dao<T,Integer> dao = ActiveRecord.getDao(ctx, table);
        boolean isSaved = false;

        if (dao != null){
            try {
                //disables auto commit before launch query and reenables after finish
                isSaved = dao.callBatchTasks(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        for (T t : list) {
                            boolean createdOrUpdated = ActiveRecord.isCreatedOrUpdated(dao.createOrUpdate(t));
                            if (!createdOrUpdated) {
                                if (DEBUG) {
                                    e(TAG, "cannot save or update list entry");
                                }
                                return false;
                            }
                        }
                        return true;
                    }
                });
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
                e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }finally {
                releaseDao();
            }
        }
        return isSaved;
    }

    /**
     * save or update current item list. Only use with obtainHelper before and releasehelper after block of operations
     * @param list
     * @param <T>
     * @return
     */
    public static<T extends ActiveRecord> boolean saveAll(final List<T> list){
        if (list.isEmpty()){
            if (DEBUG) {
                Log.d(TAG, "saveAllBulk nothing to save, list is empty");
            }
            return false;
        }
        Class table = list.get(0).getClass();
        final Dao<T,Integer> dao = ActiveRecord.getCachedDao(table);
        boolean isSaved = false;

        if (dao != null){
            try {
                //disables auto commit before launch query and reenables after finish
                isSaved = dao.callBatchTasks(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        for (T t : list) {
                            boolean createdOrUpdated = ActiveRecord.isCreatedOrUpdated(dao.createOrUpdate(t));
                            if (!createdOrUpdated) {
                                if (DEBUG) {
                                    e(TAG, "cannot save or update list entry");
                                }
                                return false;
                            }
                        }
                        return true;
                    }
                });
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
                e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }
        }
        return isSaved;
    }
    
    public static<T extends ActiveRecord> int delete(Context ctx, T t){
    	if (t == null){
            if (DEBUG) {
                d(TAG, "cannot delete object because its null");
            }
    		return 0;
    	}
    	Class table = t.getClass();
        final Dao<T,Integer> dao = ActiveRecord.getDao(ctx, table);
        int delRows = 0;
        if (dao != null){
            try {
               delRows = dao.delete(t);
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
            	e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }finally {
                releaseDao();
            }
        }
        return delRows;
    }
    
    public static<T extends ActiveRecord> int deleteList(Context ctx, List<T> t){
    	if (t == null || t.isEmpty()){
            if (DEBUG) {
                e(TAG, "cannot delete collection because its null or empty");
            }
    		return 0;
    	}
    	Class table = t.get(0).getClass();
        final Dao<T,Integer> dao = ActiveRecord.getDao(ctx, table);
        int delRows = 0;
        if (dao != null){
            try {
               delRows = dao.delete(t);
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
            	e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }finally {
                releaseDao();
            }
        }
        return delRows;
    }

    public static<T extends ActiveRecord> int deleteAll(Context ctx, Class table){
        final Dao<T,Integer> dao = ActiveRecord.getDao(ctx, table);
        int delRows = 0;
        if (dao != null){
            try {
               delRows = dao.deleteBuilder().delete();
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
            	e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }finally {
                releaseDao();
            }
        }
        return delRows;
    }

    public static<T extends ActiveRecord> int deleteByField(Context ctx, Class table, String field, Object value){
        final Dao<T,Integer> dao = ActiveRecord.getDao(ctx, table);
        int delRows = 0;
        if (dao != null){
            try {
                DeleteBuilder<T, Integer> deleteBuilder = dao.deleteBuilder();
                deleteBuilder.where().eq(field, value);
                delRows = deleteBuilder.delete();
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
            	e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }finally {
                releaseDao();
            }
        }
        return delRows;
    }

    public static<T extends ActiveRecord> List<T> where(Context ctx, Class table, String field, int whereType, Object value){
        final Dao<T,Integer> dao = ActiveRecord.getDao(ctx, table);
        List<T> list = null;
        if (dao != null){
            try {
                // get our query builder from the DAO
                QueryBuilder<T, Integer> queryBuilder =dao.queryBuilder();
                if (whereType == EQ){
                	queryBuilder.where().eq(field, value);
                }else if (whereType == N_EQ){
                	queryBuilder.where().ne(field, value);
                }else if (whereType == GEQ){
                	queryBuilder.where().ge(field, value);
                }else if (whereType == LEQ){
                	queryBuilder.where().le(field, value);
                }else if (whereType == LT){
                	queryBuilder.where().lt(field, value);
                }else if (whereType == GT){
                	queryBuilder.where().gt(field, value);
                }
                 
                
                /*
                else if (whereType == ){
               	//TODO: here goes most operators, under building
               }
                */
                
                // prepare our sql statement
                PreparedQuery<T> preparedQuery = queryBuilder.prepare();
                list = dao.query(preparedQuery);
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
            	e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }finally {
                releaseDao();
            }
        }
        if (list == null){
            list = new ArrayList<>();
        }
        return list;
    }
    
    public static<T extends ActiveRecord> List<T> whereMultiple(Context ctx, Class table, List<String> fieldList, List<Integer> whereTypeList, List<Object> valueList, List<Integer> operatorsList) {
    	if (fieldList == null || whereTypeList == null || valueList == null || operatorsList == null) {
            if (DEBUG) {
                e(TAG, "Params are null");
            }
    		return new ArrayList<>();
    	}
    	
    	if (fieldList.size() != valueList.size() || fieldList.size() != whereTypeList.size() || fieldList.size() != (operatorsList.size() + 1)) {
            if (DEBUG) {
                e(TAG, "Params are not equals size or whereTypeList doesnt match params size");
            }
    		return new ArrayList<T>();
    	}
    	
        final Dao<T,Integer> dao = ActiveRecord.getDao(ctx, table);
        List<T> list = null;
        if (dao != null){
            try {
                // get our query builder from the DAO
                QueryBuilder<T, Integer> queryBuilder = dao.queryBuilder();
                Where<T,Integer> where = queryBuilder.where();
                
                for (int i = 0; i < fieldList.size(); i++) {
                	String field = fieldList.get(i);
                	int whereType = whereTypeList.get(i);
                	Object value = valueList.get(i);
                	
              
                	if (whereType == EQ){
                    	where.eq(field, value);
                    }else if (whereType == N_EQ){
                    	where.ne(field, value);
                    }else if (whereType == GEQ){
                    	where.ge(field, value);
                    }else if (whereType == LEQ){
                    	where.le(field, value);
                    }else if (whereType == LT){
                    	where.lt(field, value);
                    }else if (whereType == GT){
                    	where.gt(field, value);
                    }
                	
                	if (i < operatorsList.size()){
                		int operator = operatorsList.get(i);
                		if (operator == AND) {
                			where.and();
                		}else if (operator == OR) {
                			where.or();
                		}
                	}
                }
             
                PreparedQuery<T> preparedQuery = queryBuilder.prepare();
                list = dao.query(preparedQuery);
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (OutOfMemoryError e){
            	e(TAG, "out of memory", e);
            }catch (Exception e){
                e(TAG, "exception", e);
            }finally {
                releaseDao();
            }
        }
        if (list == null){
            list = new ArrayList<>();
        }
        return list;
    }

    
    public static<T extends ActiveRecord> List<T> foreignWhere(Context ctx, Class table, Class foreignTable, String foreignField, Object foreignValue){
        final Dao<T,Integer> dao = ActiveRecord.getDao(ctx, table);
        final Dao<T,Integer> foreignDao = ActiveRecord.getDao(ctx, foreignTable);
        
        List<T> list = null;
        if (dao != null && foreignDao != null){
            try {
                QueryBuilder<T, Integer> foreignQb = foreignDao.queryBuilder();
                foreignQb.where().eq(foreignField, foreignValue);
                
                QueryBuilder<T, Integer> qb = dao.queryBuilder();
                
                // join
                list = qb.join(foreignQb).query();
            } catch (SQLException e) {
                e(TAG, "sql", e);
            } catch (Exception e){
                e(TAG, "exception", e);
            } catch (OutOfMemoryError e){
            	e(TAG, "out of memory", e);
            }
            finally {
                releaseDao();
            }
        }
        if (list == null){
            list = new ArrayList<>();
        }
        return list;
    }
}
