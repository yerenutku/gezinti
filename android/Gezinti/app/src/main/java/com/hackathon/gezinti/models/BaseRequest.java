package com.hackathon.gezinti.models;

import java.io.Serializable;
import java.lang.reflect.Type;

/**
 * Created by yutku on 25/03/17.
 */

public abstract class BaseRequest  implements Serializable{
    public abstract Type getResponseType();
}
