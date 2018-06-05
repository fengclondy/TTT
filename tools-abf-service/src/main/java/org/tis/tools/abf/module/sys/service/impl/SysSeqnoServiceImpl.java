package org.tis.tools.abf.module.sys.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tis.tools.abf.module.sys.controller.request.SysSeqnoResetRequest;
import org.tis.tools.abf.module.sys.dao.SysSeqnoMapper;
import org.tis.tools.abf.module.sys.entity.SysSeqno;
import org.tis.tools.abf.module.sys.entity.enums.SeqnoReset;
import org.tis.tools.abf.module.sys.exception.SYSExceptionCodes;
import org.tis.tools.abf.module.sys.exception.SysManagementException;
import org.tis.tools.abf.module.sys.service.ISysSeqnoService;

import java.math.BigDecimal;

import static org.tis.tools.core.utils.BasicUtil.wrap;

@Service
@Transactional( rollbackFor = Exception.class)
public class SysSeqnoServiceImpl extends ServiceImpl<SysSeqnoMapper, SysSeqno> implements ISysSeqnoService {

    @Override
    public void add(SysSeqnoResetRequest sysSeqnoResetRequest) throws SysManagementException {

        SysSeqno sysSeqno = new SysSeqno();

//        SeqnoReset seqnoReset = null;
//        if ("E".equals(sysSeqnoResetRequest.getReset())){
//            seqnoReset = SeqnoReset.EVER;
//        }

        try {
            //收集参数
            sysSeqno.setSeqKey(sysSeqnoResetRequest.getSeqKey());
            sysSeqno.setSeqName(sysSeqnoResetRequest.getSeqName());
            sysSeqno.setReset(sysSeqnoResetRequest.getReset());
            sysSeqno.setResetParams(sysSeqnoResetRequest.getResetParams());
            sysSeqno.setSeqNo(BigDecimal.valueOf(Double.valueOf(sysSeqnoResetRequest.getSeqNo())));

            insert(sysSeqno);
        }catch (Exception e){
            e.printStackTrace();
            throw new SysManagementException(SYSExceptionCodes.FAILURE_WHEN_INSERT_SYS_SEQNO,wrap(e.getMessage()));
        }

    }

    @Override
    public SysSeqno change(SysSeqnoResetRequest sysSeqnoResetRequest) throws SysManagementException {

        SysSeqno sysSeqno = new SysSeqno();

        sysSeqno.setSeqKey(sysSeqnoResetRequest.getSeqKey());
        sysSeqno.setSeqName(sysSeqnoResetRequest.getSeqName());
        sysSeqno.setSeqNo(BigDecimal.valueOf(Double.valueOf(sysSeqnoResetRequest.getSeqNo())));
        sysSeqno.setReset(sysSeqnoResetRequest.getReset());
        sysSeqno.setResetParams(sysSeqnoResetRequest.getResetParams());

        try {
            updateById(sysSeqno);
        }catch (Exception e){
            e.printStackTrace();
            throw new SysManagementException(SYSExceptionCodes.FAILURE_WHEN_UPDATE_SYS_SEQNO,wrap(e.getMessage()));
        }

        return sysSeqno;
    }

    /**
     * 获取下一个序列号
     *
     * @param seqKey 序号键值
     * @return
     * @throws SysManagementException
     */
    @Override
    public long getNextSequence(String seqKey, String seqName) throws SysManagementException {
        long seq;
        // TODO 增加Redis能力
        SysSeqno sysSeqno = selectById(seqKey);
        // 如果没有该资源，新增资源
        if(sysSeqno == null) {
            SysSeqno seqno = new SysSeqno();
            seqno.setSeqKey(seqKey);
            seqno.setSeqName(seqName);
            seqno.setSeqNo(new BigDecimal("2"));
            seqno.setReset(SeqnoReset.EVER);
            insert(seqno);
            seq = 1;
        } else {
            BigDecimal curNo = sysSeqno.getSeqNo();
            sysSeqno.setSeqNo(curNo.add(new BigDecimal(1)));
            updateById(sysSeqno);
            seq = curNo.longValue();
        }
        return seq;
    }

}
