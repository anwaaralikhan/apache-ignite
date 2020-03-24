/***
* 
* Using IgniteConfiguration class we are able to configure all available Ignite’s node settings. 
* The most important thing here is a cache configuration 
* (1). We should add primary key and entity classes as an indexed types 
*        ccfg2.setIndexedTypes(Long.class, Contact.class); // (1)
* (2). Then we have to enable export cache updates to database 
*        ccfg2.setWriteBehindEnabled(true); // (2)
* (3) and read data not found in a cache from database 
*        ccfg2.setWriteThrough(true); // (2)
* (4). The interaction between Ignite’s node and Database may be configured using CacheJdbcPojoStoreFactory class 
*        ccfg2.setReadThrough(true); // (3)
*        CacheJdbcPojoStoreFactory<Long, Contact> f2 = new CacheJdbcPojoStoreFactory<>(); //5
* (5). We should pass there DataSource @Bean 
*         f2.setDataSource(datasource);
* (6), dialect 
*        f2.setDialect(new MySQLDialect()); 
* (7) and mapping between object fields and table columns 
*        JdbcType jdbcContactType = new JdbcType();
* (8).
***/

@Bean
public Ignite igniteInstance() {
   IgniteConfiguration cfg = new IgniteConfiguration();
   cfg.setIgniteInstanceName("ignite-1");
   cfg.setPeerClassLoadingEnabled(true);
 
   CacheConfiguration<Long, Contact> ccfg2 = new CacheConfiguration<>("ContactCache"); 
   ccfg2.setIndexedTypes(Long.class, Contact.class); // (1)
   ccfg2.setWriteBehindEnabled(true); // (2)
   ccfg2.setWriteThrough(true); // (3)
   ccfg2.setReadThrough(true); // (4)
   CacheJdbcPojoStoreFactory<Long, Contact> f2 = new CacheJdbcPojoStoreFactory<>(); // (5)
   f2.setDataSource(datasource); // (6)
   f2.setDialect(new MySQLDialect()); // (7)
   JdbcType jdbcContactType = new JdbcType(); // (8)
   jdbcContactType.setCacheName("ContactCache");
   jdbcContactType.setKeyType(Long.class);
   jdbcContactType.setValueType(Contact.class);
   jdbcContactType.setDatabaseTable("contact");
   jdbcContactType.setDatabaseSchema("ignite");
   jdbcContactType.setKeyFields(new JdbcTypeField(Types.INTEGER, "id", Long.class, "id"));
   jdbcContactType.setValueFields(new JdbcTypeField(Types.VARCHAR, "contact_type", ContactType.class, "type"), new JdbcTypeField(Types.VARCHAR, "location", String.class, "location"), new JdbcTypeField(Types.INTEGER, "person_id", Long.class, "personId"));
   f2.setTypes(jdbcContactType);
   ccfg2.setCacheStoreFactory(f2);
 
   CacheConfiguration<Long, Person> ccfg = new CacheConfiguration<>("PersonCache");
   ccfg.setIndexedTypes(Long.class, Person.class);
   ccfg.setWriteBehindEnabled(true);
   ccfg.setReadThrough(true);
   ccfg.setWriteThrough(true);
   CacheJdbcPojoStoreFactory<Long, Person> f = new CacheJdbcPojoStoreFactory<>();
   f.setDataSource(datasource);
   f.setDialect(new MySQLDialect());
   JdbcType jdbcType = new JdbcType();
   jdbcType.setCacheName("PersonCache");
   jdbcType.setKeyType(Long.class);
   jdbcType.setValueType(Person.class);
   jdbcType.setDatabaseTable("person");
   jdbcType.setDatabaseSchema("ignite");
   jdbcType.setKeyFields(new JdbcTypeField(Types.INTEGER, "id", Long.class, "id"));
   jdbcType.setValueFields(new JdbcTypeField(Types.VARCHAR, "first_name", String.class, "firstName"), new JdbcTypeField(Types.VARCHAR, "last_name", String.class, "lastName"), new JdbcTypeField(Types.VARCHAR, "gender", Gender.class, "gender"), new JdbcTypeField(Types.VARCHAR, "country", String.class, "country"), new JdbcTypeField(Types.VARCHAR, "city", String.class, "city"), new JdbcTypeField(Types.VARCHAR, "address", String.class, "address"), new JdbcTypeField(Types.DATE, "birth_date", Date.class, "birthDate"));
   f.setTypes(jdbcType);
   ccfg.setCacheStoreFactory(f);
 
   cfg.setCacheConfiguration(ccfg, ccfg2);
   return Ignition.start(cfg);
}
