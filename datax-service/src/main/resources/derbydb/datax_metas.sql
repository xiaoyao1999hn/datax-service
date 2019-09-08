CREATE SCHEMA datax_metas AUTHORIZATION datax_metas;

CREATE TABLE datax_report (
  jobId bigint,
  jobName varchar(256) DEFAULT '',
  jobState int DEFAULT 0,
  progress int DEFAULT 0,
  vmInfo CLOB ,
  taskCount int DEFAULT 0,
  startTimeStamp bigint DEFAULT 0,
  endTimeStamp bigint DEFAULT 0,
  startTransferTimeStamp bigint DEFAULT 0,
  endTransferTimeStamp bigint DEFAULT 0,
  createTime bigint DEFAULT 0,
  runTimes varchar(50) DEFAULT '',
  avgFlow varchar(50) DEFAULT '',
  speed varchar(50) DEFAULT '',
  totalRecordCount varchar(50) DEFAULT '',
  totalFailRecordCount varchar(50) DEFAULT '',
  totalWriteRecordCount varchar(50) DEFAULT '',
  totalReadBytes varchar(50) DEFAULT '',
  totalWriteBytes varchar(50) DEFAULT '',
  status int DEFAULT 0,
  failCount varchar(50) DEFAULT '',
  exceptionLog CLOB ,
  logStatistics CLOB,
  configurationInfo CLOB,
  remark varchar(256) DEFAULT ''
  );