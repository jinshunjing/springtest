
DROP TABLE IF EXISTS t_tx_data;
CREATE TABLE  IF NOT EXISTS t_tx_data(
  id INT(11) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  txid VARCHAR(80) NOT NULL COMMENT '链上交易记录ID',
  vout INT(1) NOT NULL DEFAULT 0 COMMENT '输出序号',
  address VARCHAR(44) NOT NULL COMMENT '地址',
  amount DECIMAL(32) NOT NULL DEFAULT 0 COMMENT '金额',
  state INT(2) NOT NULL DEFAULT 0 COMMENT '状态',
  create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  modify_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE INDEX t_tx_data_idx_1 ON t_tx_data(txid, vout);
