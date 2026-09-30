package app.mrm.metromate.nfc

import app.mrm.metromate.model.CardReadResult
import app.mrm.metromate.model.Transaction
import app.mrm.metromate.nfc.parser.ByteParser
import app.mrm.metromate.nfc.parser.TransactionParser
import app.mrm.metromate.nfc.service.TimestampService

/**
 * Shared read orchestration used by both platforms. Reads the two 10-block windows
 * (blocks 0–9, then 10–19) over the transaction-history service, skips any window with
 * non-zero status flags, parses and validity-filters the blocks, and returns what it
 * collected. If the transceiver throws mid-read, the transactions gathered so far are
 * returned (partial-result semantics matching Android's original IOException handling).
 */
class FelicaReader(
    private val transceiver: CardTransceiver,
) {
    suspend fun readTransactionHistory(baseYear: Int = TimestampService.currentBaseYear()): CardReadResult {
        val transactions = mutableListOf<Transaction>()
        try {
            for (startBlock in READ_WINDOWS) {
                val result = transceiver.readBlocks(SERVICE_CODE, startBlock, BLOCKS_PER_WINDOW)
                transactions += TransactionParser.parseValidTransactions(result, baseYear)
            }
        } catch (_: CardTransceiverException) {
            // Partial-result semantics: return the transactions gathered before the read failed.
        }
        return CardReadResult(
            idm = ByteParser.toHexString(transceiver.idm),
            transactions = transactions,
        )
    }

    companion object {
        const val SERVICE_CODE = 0x220F
        const val BLOCKS_PER_WINDOW = 10
        private val READ_WINDOWS = listOf(0, 10)
    }
}
