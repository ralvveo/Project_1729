package com.example.project1729.domain.converter

import com.example.project1729.data.db.entity.CheckEntity
import com.example.project1729.data.db.entity.TestEntity
import com.example.project1729.domain.model.Check
import com.example.project1729.domain.model.Test

object Converter {

    fun convert(check: Check): CheckEntity{
        return CheckEntity(
            checkId = check.checkId,
            KCHSM = check.KCHSM,
            dateAndTime = check.dateAndTime,
            eye = check.eye,
            measurementMethod = check.measurementMethod,
            diodeSaturation = check.diodeSaturation,
            diodeSize = check.diodeSize,
            diodeColor = check.diodeColor,
            roomBrightness = check.roomBrightness,
            roomPressure = check.roomPressure,
            roomTemperature = check.roomTemperature,
            roomHumidity = check.roomHumidity,
            eyeLoading = check.eyeLoading,
            eyeLoadingDuration = check.eyeLoadingDuration,
            eyeTraining = check.eyeTraining

        )
    }


    fun convert(checkEntity: CheckEntity): Check{
        return Check(
            checkId = checkEntity.checkId,
            KCHSM = checkEntity.KCHSM,
            dateAndTime = checkEntity.dateAndTime,
            eye = checkEntity.eye,
            measurementMethod = checkEntity.measurementMethod,
            diodeSaturation = checkEntity.diodeSaturation,
            diodeSize = checkEntity.diodeSize,
            diodeColor = checkEntity.diodeColor,
            roomBrightness = checkEntity.roomBrightness,
            roomPressure = checkEntity.roomPressure,
            roomTemperature = checkEntity.roomTemperature,
            roomHumidity = checkEntity.roomHumidity,
            eyeLoading = checkEntity.eyeLoading,
            eyeLoadingDuration = checkEntity.eyeLoadingDuration,
            eyeTraining = checkEntity.eyeTraining

        )
    }

    fun convert(testEntity: TestEntity): Test {
        return Test(
            testId = testEntity.testId,
            type = testEntity.type,
            dateAndTime = testEntity.dateAndTime,
            result = testEntity.result
        )

    }


    fun convert(test: Test): TestEntity {
        return TestEntity(
            testId = test.testId,
            type = test.type,
            dateAndTime = test.dateAndTime,
            result = test.result
        )

    }
}