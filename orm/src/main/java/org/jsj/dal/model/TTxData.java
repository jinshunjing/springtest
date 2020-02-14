package org.jsj.dal.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@AllArgsConstructor
@Builder
@Getter
@ToString
@NoArgsConstructor
public class TTxData implements Serializable {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_tx_data.id
     *
     * @mbg.generated
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_tx_data.txid
     *
     * @mbg.generated
     */
    private String txid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_tx_data.vout
     *
     * @mbg.generated
     */
    private Integer vout;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_tx_data.address
     *
     * @mbg.generated
     */
    private String address;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_tx_data.amount
     *
     * @mbg.generated
     */
    private BigDecimal amount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_tx_data.state
     *
     * @mbg.generated
     */
    private Integer state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_tx_data.create_time
     *
     * @mbg.generated
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_tx_data.modify_time
     *
     * @mbg.generated
     */
    private Date modifyTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_tx_data
     *
     * @mbg.generated
     */
    private static final long serialVersionUID = 1L;

    @AllArgsConstructor
    public enum COLUMNS {
        ID("id"),
        TXID("txid"),
        VOUT("vout"),
        ADDRESS("address"),
        AMOUNT("amount"),
        STATE("state"),
        CREATE_TIME("create_time"),
        MODIFY_TIME("modify_time");

        @Getter
        private String column;
    }
}