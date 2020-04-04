package modules;

import com.google.inject.name.Names;
import mapper.CarMarkMapper;
import mapper.CarModelMapper;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import play.db.Database;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.sql.DataSource;
import java.util.Properties;

public class MyBatisModule extends org.mybatis.guice.MyBatisModule {

    @Override
    protected void initialize() {

        bindDataSourceProviderType(PlayDataSourceProvider.class);
        bindTransactionFactoryType(JdbcTransactionFactory.class);

        final Properties myBatisProperties = new Properties();
        myBatisProperties.setProperty("mybatis.environment.id", "default");
        Names.bindProperties(binder(), myBatisProperties);

        addMapperClass(CarMarkMapper.class);
        addMapperClass(CarModelMapper.class);

    }

    static class PlayDataSourceProvider implements Provider<DataSource> {

        final Database db;

        @Inject
        public PlayDataSourceProvider(final Database db) {
            this.db = db;
        }

        @Override
        public DataSource get() {
            // use db as configured in conf/application.conf
            return db.getDataSource();
        }

    }
}
