#!/bin/bash

# /usr/bin/basic-sample
BIN_A="/usr/bin/basic-sample"
BIN_B="/usr/bin/basic-sample-b"

LAST_USED_DATA="/home/iotc-c-sdk_last-image-str.log"

BIN_A_STR="A"
BIN_B_STR="B"

CURRENT_BINARY="${BIN_A}"
CURRENT_STR="${BIN_A_STR}"

while IFS= read -r line
do
    echo "$line"

    if [ "$line" == "$BIN_A_STR" ]
    then
        CURRENT_STR="$BIN_A_STR"
        CURRENT_BINARY="$BIN_A"
    elif [ "$line" == "$BIN_B_STR" ]
    then
        CURRENT_STR="$BIN_B_STR"
        CURRENT_BINARY="$BIN_B"
    else
        echo "invalid parameters in logfile"
        exit 1
    fi

done < "$LAST_USED_DATA"

RET_CODE=0

for (( ; ; ))
do
    $CURRENT_BINARY "$CURRENT_STR"
    RET_CODE=$?
    echo $RET_CODE
    # switch to other binary if
    if [ $RET_CODE == 5 ] #placeholder?
    then
        if [ "$CURRENT_BINARY" == "$BIN_A" ]
        then
            chmod +x $BIN_B
            CURRENT_BINARY="$BIN_B"
            CURRENT_STR="$BIN_B_STR"
            echo "A" > $LAST_USED_DATA
        elif [ "$CURRENT_BINARY" == "$BIN_B" ]
        then
            CURRENT_BINARY="$BIN_A"
            chmod +x $BIN_A
            CURRENT_STR="$BIN_A_STR"
            echo "B" > $LAST_USED_DATA
        else
            echo "do nothing"
        fi
    else
        echo "other return code"
    fi
done
