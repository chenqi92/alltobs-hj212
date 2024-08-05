package com.alltobs.hj212.model.verify;


import com.alltobs.hj212.model.verify.groups.ModeGroup;
import com.alltobs.hj212.model.verify.groups.VersionGroup;
import com.alltobs.hj212.validator.clazz.FieldC;
import com.alltobs.hj212.validator.clazz.FieldN;
import com.alltobs.hj212.validator.clazz.FieldValidDate;
import com.alltobs.hj212.validator.field.C;
import com.alltobs.hj212.validator.field.N;
import com.alltobs.hj212.validator.field.ValidDate;

import java.util.Map;

/**
 * T212 Map
 * 解决无法对MAP进行验证定义问题
 *
 * @author ChenQi
 */
@FieldValidDate(field = "QN",
        value = @ValidDate(format = "yyyyMMddHHmmssSSS"))
@FieldC(field = "ST",
        value = @C(len = 2))
@FieldC(field = "CN",
        value = @C(len = 4))
@FieldC(field = "PW",
        value = @C(len = 6))
@FieldC(field = "MN",
        value = @C(len = 14))
@FieldN(field = "Flag",
        value = @N(integer = 3))
@FieldN(field = "PNUM", groups = ModeGroup.UseSubPacket.class,
        value = @N(integer = 4, optional = false))
@FieldN(field = "PNO", groups = ModeGroup.UseSubPacket.class,
        value = @N(integer = 4, optional = false))
@FieldC(field = "CP", groups = {VersionGroup.V2005.class},
        value = @C(len = 960))
@FieldC(field = "CP", groups = {VersionGroup.V2017.class},
        value = @C(len = 950))
public class T212DataLevelMap
        extends T212Map<String, String> {

    public T212DataLevelMap(Map<String, String> m) {
        super(m);
    }
}
