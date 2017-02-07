package com.jajale.watch.entitydb;

import android.text.TextUtils;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.jajale.watch.utils.CMethod;
import com.jajale.watch.utils.L;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * 主要用到的DB操作都在这里 SqliteHelper只是负责建表和升级表
 *
 *
 * Created by athena on 2015/11/11.
 * Email: lizhiqiang@bjjajale.com
 */
public class DbHelper<T> {

    private Class<T> clazz;
    private OrmLiteSqliteOpenHelper db;

    public DbHelper(OrmLiteSqliteOpenHelper db, Class<T> clazz) {
        this.db = db;
        this.clazz = clazz;
    }

    /**
     * 新增一条记录
     */
    public int create(T po) {
        try {
            Dao dao = db.getDao(clazz);
            return dao.create(po);
        } catch (SQLException e) {
            L.e(e);
        }
        return -1;
    }

    /**
     * 新增一条不存在的数据记录，若存在相同数据则不添加
     */
//    public Object createNotExists(T po) {
//        try {
//            Dao dao = db.getDao(clazz);
//            return dao.createIfNotExists(po);
//        } catch (SQLException e) {
//            L.e(e);
//        }
//        return null;
//    }

    public boolean exists(T po, Map<String, Object> where) {
        try {
            Dao dao = db.getDao(po.getClass());
            if (dao.queryForFieldValues(where).size() > 0) {
                return true;
            }
        } catch (SQLException e) {
            L.e(e);
        }
        return false;
    }

    public T getObject(String id) {
        try {
            Dao dao = db.getDao(clazz);
            if (dao.idExists(id)) {
                return (T) dao.queryForId(id);
            }
        } catch (SQLException e) {
            L.e(e);
        }
        return null;
    }

    public int createIfNotExists(T po, Map<String, Object> where) {
        try {
            Dao dao = db.getDao(po.getClass());
            if (dao.queryForFieldValues(where).size() < 1) {
                return dao.create(po);
            }
        } catch (SQLException e) {
            L.e(e);
        }
        return -1;
    }

    public int createIfNotExists(T po) {
        try {
            Dao dao = db.getDao(clazz);
            dao.createIfNotExists(po);
            return 1;
        } catch (SQLException e) {
            L.e(e);
        }
        return -1;
    }

    public List<T> queryForEq(String fieldName, Object value) {
        try {
            Dao dao = db.getDao(clazz);
            return dao.queryForEq(fieldName, value);
        } catch (SQLException e) {
            L.e(e);
        }
        return new ArrayList<T>();
    }

    public List<T> queryForEq(Map<String, Object> map) {
        try {
            if (map != null && !map.isEmpty()) {

                Dao dao = db.getDao(clazz);
                Where where = dao.queryBuilder().where();

                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    Object object = map.get(next);
                    where.eq(next, object);
                    if (iterator.hasNext()) {
                        where.and();
                    }
                }
                return where.query();
            }
        } catch (SQLException e) {
            L.e(e);
        }
        return new ArrayList<T>();
    }

    public Dao getDao() {
        try {
            return db.getDao(clazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取最后一条数据
     *
     * @param culumn 可以用来做排序的字段
     * @return
     */
    public T getLastEntity(String culumn) {
        try {
            Dao dao = db.getDao(clazz);
            List query = dao.queryBuilder().orderBy(culumn, false).limit(1L).query();
            return (T) ((query != null && query.size() > 0) ? query.get(0) : null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 倒叙按条件分页查找
     *
     * @param map
     * @param pagesize
     * @param page
     * @return
     */
    public List<T> queryFromBack(Map<String, Object> map, long pagesize, int page, String orderByRaw) {
        try {
            Dao dao = db.getDao(clazz);
            QueryBuilder queryBuilder = dao.queryBuilder();
            StringBuffer whereSB;
            if (map != null && !map.isEmpty()) {
                Where where = queryBuilder.where();
                whereSB = new StringBuffer("select count(*) from " + clazz.getSimpleName() + " where ");
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    Object object = map.get(next);
                    whereSB.append(next + "=" + object + "");
                    where.eq(next, object);
                    if (iterator.hasNext()) {
                        where.or();
                        whereSB.append(" or ");
                    }
                }
            } else {
                whereSB = new StringBuffer("select count(*) from " + clazz.getSimpleName());
            }
            GenericRawResults<String[]> countResults = dao.queryRaw(whereSB.toString());
            String[] strings = countResults.getResults().get(0);
            int total = Integer.parseInt(strings[0]);

            if (total + pagesize > page * pagesize) {
                long start = total - pagesize * (page - 1) - pagesize;
                if (start < 0) {
                    pagesize = pagesize + start;
                    start = 0;
                }
                queryBuilder.limit(pagesize).offset(start);
                if (!TextUtils.isEmpty(orderByRaw)) {
                    queryBuilder.orderByRaw(orderByRaw);
                }
                return queryBuilder.query();
            }
        } catch (SQLException e) {
            L.e(e);
        }
        return new ArrayList<T>();
    }

    public List<T> query(Map<String, Object> map, long pagesize, int page, String orderByRaw) {
        try {
            Dao dao = db.getDao(clazz);
            QueryBuilder queryBuilder = dao.queryBuilder();
            if (map != null && !map.isEmpty()) {
                Where where = queryBuilder.where();
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    Object object = map.get(next);
                    where.eq(next, object);
                    if (iterator.hasNext()) {
                        where.or();
                    }
                }
            }

            queryBuilder.limit(pagesize).offset((page - 1) * pagesize);
            if (!TextUtils.isEmpty(orderByRaw)) {
                queryBuilder.orderBy(orderByRaw, false);
            }
            return queryBuilder.query();
        } catch (SQLException e) {
            L.e(e);
        }
        return new ArrayList<T>();
    }

    /**
     * 按条件查
     *
     * @param map
     * @param pagesize
     * @param page
     * @param orderByRaw 该字段降序
     * @return
     */
//    public List<T> queryDesc(Map<String, Object> map, long pagesize, int page, String orderByRaw) {
//        return query(map, pagesize, page, orderByRaw, false);
//    }

    /**
     * 按条件查
     *
     * @param map
     * @param pagesize
     * @param page
     * @param orderByRaw 该字段升序
     * @return
     */
//    public List<T> queryAsc(Map<String, Object> map, long pagesize, int page, String orderByRaw) {
//        return query(map, pagesize, page, orderByRaw, true);
//    }

    /**
     * @param map
     * @param pagesize
     * @param page
     * @param orderByRaw
     * @param desc       是否降序
     * @return
     */
    private List<T> query(Map<String, Object> map, long pagesize, int page, String orderByRaw, boolean desc) {
        try {
            Dao dao = db.getDao(clazz);
            QueryBuilder queryBuilder = dao.queryBuilder();

            if (map != null && !map.isEmpty()) {
                Where where = queryBuilder.where();
                Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> next = iterator.next();
                    where.eq(next.getKey(), next.getValue());
                    if (iterator.hasNext()) {
                        where.and();
                    }
                }
            }

            long start = pagesize * (page - 1);
            queryBuilder.limit(pagesize).offset(start);
            if (!TextUtils.isEmpty(orderByRaw)) {
                queryBuilder.orderBy(orderByRaw, desc);
            }
            return queryBuilder.query();
        } catch (SQLException e) {
            L.e(e);
        }
        return new ArrayList<T>();
    }

    //    public List<T> query(long pagesize, int page) {
//        try {
//            Dao dao = db.getDao(clazz);
//            QueryBuilder queryBuilder = dao.queryBuilder();
//            queryBuilder.limit(pagesize).offset((page - 1) * pagesize);
//            return queryBuilder.query();
//        } catch (SQLException e) {
//            L.e(e);
//        }
//        return new ArrayList<T>();
//    }

    /**
     * 倒叙按分页查找
     *
     * @param pagesize
     * @param page
     * @return
     * @author yuancl Jun 11, 2014
     */
//    public List<T> queryFromBack(long pagesize, int page) {
//        return queryFromBack(null, pagesize, page, null);
//    }

//    public List<T> query(long pagesize, int page, String orderByRaw) {
//        return query(null, pagesize, page, orderByRaw);
//    }

    /**
     * 删除一条记录
     */
    public int remove(T po) {
        try {
            Dao dao = db.getDao(clazz);
            return dao.delete(po);
        } catch (SQLException e) {
            L.e(e);
        }
        return -1;
    }

//    public int deleteBy(String columnName, String value) {
//        try {
//            Dao dao = db.getDao(clazz);
//            return dao.executeRawNoArgs("delete from " + clazz.getSimpleName() + " where " + columnName + "=" + value);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }

    /**
     * 删除一条记录
     */
    public int delete(T t) {
        try {
            Dao dao = db.getDao(clazz);
            return dao.delete(t);
        } catch (SQLException e) {
            L.e(e);
        }
        return -1;
    }

    /**
     * 删除一堆记录
     */
    public void remove(List<T> list) {
        try {
            Dao dao = db.getDao(clazz);
            dao.delete(list);
        } catch (SQLException e) {
            L.e(e);
        }
    }

    /**
     * 删除所有记录
     */
    public void clear() {
        try {
            Dao dao = db.getDao(clazz);
            dao.executeRawNoArgs("delete from " + clazz.getSimpleName());
        } catch (SQLException e) {
            L.e(e);
        }
    }

    /**
     * 删除一条记录
     */
//    public void deleteImageById(int id) {
//        try {
//            Dao dao = db.getDao(clazz);
//            dao.executeRawNoArgs("delete from " + clazz.getSimpleName() + " where id=" + id);
//        } catch (SQLException e) {
//            L.e(e);
//        }
//    }

    /**
     * 根据特定条件更新特定字段
     *
     * @param values
     * @param columnName where字段
     * @param value      where值
     * @return
     */
    public int update(Map<String, Object> values, String columnName, Object value) {
        try {
            Dao dao = db.getDao(clazz);
            UpdateBuilder<T, Long> updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq(columnName, value);

            for (String key : values.keySet()) {
                updateBuilder.updateColumnValue(key, values.get(key));
            }
            return updateBuilder.update();
        } catch (SQLException e) {
            L.e(e);
        }
        return -1;
    }

//    public long queryCountForOr(Map<String, Object> map) {
//        try {
//            if (map != null && !map.isEmpty()) {
//
//                Dao dao = db.getDao(clazz);
//                Where where = dao.queryBuilder().where();
//
//                Iterator<String> iterator = map.keySet().iterator();
//                while (iterator.hasNext()) {
//                    String next = iterator.next();
//                    Object object = map.get(next);
//                    where.eq(next, object);
//                    if (iterator.hasNext()) {
//                        where.or();
//                    }
//                }
//                return dao.countOf(where.prepare());
//            }
//        } catch (SQLException e) {
//            L.e(e);
//        }
//        return 0;
//    }

    public int update(T po) {
        try {
            Dao dao = db.getDao(clazz);
            return dao.update(po);
        } catch (SQLException e) {
            L.e(e);
        }
        return -1;
    }

    /**
     * 查询所有记录
     */
    public List<T> queryForAll() {
        try {
            Dao dao = db.getDao(clazz);
            return dao.queryForAll();
        } catch (SQLException e) {
            L.e(e);
        }
        return new ArrayList<T>();
    }

    /**
     * 根据where条件查该列内容总和
     *
     * @param column
     * @param paramets
     * @return
     */
    public int sum(String column, Map<String, String> paramets) {
        try {
            Dao dao = db.getDao(clazz);
            StringBuilder sb = new StringBuilder();
            if (paramets != null && paramets.size() > 0) {
                Iterator<String> iterator = paramets.keySet().iterator();
                sb.append(" where ");
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    String value = paramets.get(next);
                    sb.append(next).append("=").append(value);
                    if (iterator.hasNext()) {
                        sb.append(" and");
                    }
                }
            }
            GenericRawResults<String[]> countResults = dao.queryRaw("select sum(" + column + ") from " + clazz.getSimpleName() + sb.toString());
            String[] strings = countResults.getResults().get(0);
            return (strings.length > 0 && !CMethod.isEmptyOrZero(strings[0])) ? Integer.parseInt(strings[0]) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int sumNotEqual(String column, Map<String, String> paramets) {
        try {
            Dao dao = db.getDao(clazz);
            StringBuilder sb = new StringBuilder();
            if (paramets != null && paramets.size() > 0) {
                Iterator<String> iterator = paramets.keySet().iterator();
                sb.append(" where ");
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    String value = paramets.get(next);
                    sb.append(next).append("<>").append(value);
                    if (iterator.hasNext()) {
                        sb.append(" and");
                    }
                }
            }
            GenericRawResults<String[]> countResults = dao.queryRaw("select sum(" + column + ") from " + clazz.getSimpleName() + sb.toString());
            String[] strings = countResults.getResults().get(0);
            return (strings.length > 0 && !CMethod.isEmptyOrZero(strings[0])) ? Integer.parseInt(strings[0]) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据where条件查该列数量
     *
     * @param paramets
     * @return
     */
    public int count(Map<String, String> paramets) {
        try {
            Dao dao = db.getDao(clazz);
            StringBuilder sb = new StringBuilder();
            if (paramets != null && paramets.size() > 0) {
                Iterator<String> iterator = paramets.keySet().iterator();
                sb.append(" where ");
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    String value = paramets.get(next);
                    sb.append(next).append("=").append(value);
                    if (iterator.hasNext()) {
                        sb.append(" and ");
                    }
                }
            }
            GenericRawResults<String[]> countResults = dao.queryRaw("select count(1) from " + clazz.getSimpleName() + sb.toString());
            String[] strings = countResults.getResults().get(0);
            return (strings.length > 0 && !CMethod.isEmptyOrZero(strings[0])) ? Integer.parseInt(strings[0]) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

//    public int countNotEqual(Map<String, String> paramets) {
//        try {
//            Dao dao = db.getDao(clazz);
//            StringBuilder sb = new StringBuilder();
//            if (paramets != null && paramets.size() > 0) {
//                Iterator<String> iterator = paramets.keySet().iterator();
//                sb.append(" where ");
//                while (iterator.hasNext()) {
//                    String next = iterator.next();
//                    String value = paramets.get(next);
//                    sb.append(next).append("<>").append(value);
//                    if (iterator.hasNext()) {
//                        sb.append(" and");
//                    }
//                }
//            }
//            GenericRawResults<String[]> countResults = dao.queryRaw("select count(1) from " + clazz.getSimpleName() + sb.toString());
//            String[] strings = countResults.getResults().get(0);
//            return (strings.length > 0 && !CMethod.isEmptyOrZero(strings[0])) ? Integer.parseInt(strings[0]) : 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }


//    public int limit(Map<String, String> paramets) {
//        try {
//            Dao dao = db.getDao(clazz);
//            StringBuilder sb = new StringBuilder();
//            if (paramets != null && paramets.size() > 0) {
//                Iterator<String> iterator = paramets.keySet().iterator();
//                sb.append(" where ");
//                while (iterator.hasNext()) {
//                    String next = iterator.next();
//                    String value = paramets.get(next);
//                    sb.append(next).append("=").append(value);
//                    if (iterator.hasNext()) {
//                        sb.append(" and");
//                    }
//                }
//            }
//            GenericRawResults<String[]> countResults = dao.queryRaw("select count(1) from " + clazz.getSimpleName() + sb.toString());
//            String[] strings = countResults.getResults().get(0);
//            return (strings.length > 0 && !CMethod.isEmptyOrZero(strings[0])) ? Integer.parseInt(strings[0]) : 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

    public int count(Map<String, String> paramets, String columname, String left_value, String right_value) {
        try {
            Dao dao = db.getDao(clazz);
            StringBuilder sb = new StringBuilder();
            if (paramets != null && paramets.size() > 0) {
                Iterator<String> iterator = paramets.keySet().iterator();
                sb.append(" where ");
                while (iterator.hasNext()) {
                    String next = iterator.next();
                    String value = paramets.get(next);
                    sb.append(next).append("=").append(value);
                    if (iterator.hasNext() || (!CMethod.isEmpty(columname) && !CMethod.isEmpty(left_value) && !CMethod.isEmpty(right_value))) {
                        sb.append(" and");
                    }
                }
            }
            if (!CMethod.isEmpty(columname) && !CMethod.isEmpty(left_value) && !CMethod.isEmpty(right_value)) {
                if (sb.indexOf("where") <= 0) {
                    sb.append(" where ");
                }
                sb.append(columname);
                sb.append(" > ");
                sb.append(left_value);
                sb.append(" and ");
                sb.append(columname);
                sb.append(" < ");
                sb.append(right_value);
            }
            GenericRawResults<String[]> countResults = dao.queryRaw("select count(1) from " + clazz.getSimpleName() + sb.toString());
            String[] strings = countResults.getResults().get(0);
            return (strings.length > 0 && !CMethod.isEmptyOrZero(strings[0])) ? Integer.parseInt(strings[0]) : 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
