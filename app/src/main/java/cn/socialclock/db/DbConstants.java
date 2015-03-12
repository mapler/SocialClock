package cn.socialclock.db;


/**
 * Created by mapler on 2015/03/12.
 * Constants about db
 */
public final class DbConstants {
    /** collects create table queries */
    public static final String[] CREATE_TABLE_QUERIES = {
            AlarmEventDbAdapter.CREATE_TABLE_QUERY,
    };
    /** collects drop table queries */
    public static final String[] DROP_TABLE_QUERIES = {
            AlarmEventDbAdapter.DROP_TABLE_QUERY
    };
}
