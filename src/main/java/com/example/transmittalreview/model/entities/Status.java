package com.example.transmittalreview.model.entities;

//TODO: Spin off missing with NEW, EXISTS, ERROR for second column? Potentially add isNew boolean to PART
public enum Status {
    CORRECT, //OK
    MISMATCH,//wrong rev
    MISSING, //missing
    DEFAULT
}
