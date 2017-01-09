/*
 * Copyright (c) 2017. Junya Ogasawara. All right reserved.
 *
 *  @author Junya Ogasawara
 */

package io.github.a0gajun.weather;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by Junya Ogasawara on 1/9/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants =  BuildConfig.class, sdk = 21, application = TestApplication.class)
public class ApplicationTestCase {
}
