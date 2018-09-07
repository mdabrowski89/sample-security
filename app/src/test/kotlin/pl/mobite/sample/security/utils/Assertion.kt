package pl.mobite.sample.security.utils

import org.junit.Assert
import pl.mobite.sample.security.ui.components.secretkey.SecretKeyViewState


fun assertSecretKeyViewState(expected: SecretKeyViewState, tested: SecretKeyViewState) {
    Assert.assertEquals(expected.secretKeyAlias, tested.secretKeyAlias)
    Assert.assertEquals(expected.messageEncrypted, tested.messageEncrypted)
    Assert.assertEquals(expected.messageDecrypted, tested.messageDecrypted)
    Assert.assertEquals(expected.isLoading, tested.isLoading)
    Assert.assertEquals(expected.clearMessage.get(), tested.clearMessage.get())
    Assert.assertEquals(expected.error?.throwable, tested.error?.throwable)
}