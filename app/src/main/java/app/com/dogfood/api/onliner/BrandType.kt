package app.com.dogfood.api.onliner

enum class BrandType(val type: String, var viewName: String) {

    EVERYTHING("", ""),
    ROYAL_CANIN("royalcanin", "Royal Canin"),
    PROPLAN("proplan", "Pro Plan"),
    ACANA("acana", "Acana"),
    BRIT("brit", "Brit"),
    HILLS("hills", "Hill's"),
    ANIMONDA("animonda", "Animonda"),
    BERKLEY("berkley", "Berkley"),
    BOSCH("bosch", "Bosch"),
    FELIX("felix", "Felix"),
    FARMINA("farmina", "Farmina"),
    EUROCAT("eurocat", "Eurocat"),
    GOSBI("gosbi", "Gosbi"),
    KITEKAT("kitekat", "kitekat"),
    NUEVO("nuevo", "Nuevo"),
    PROBALANCE("probalance", "Probalance"),
    TASTY("tasty", "Tasty"),
    TITBIT("titbit", "TiTBiT"),
    SIRUS("sirus", "Sirius"),
    VIVO("vivo", "Vivo"),
    WHISKAS("whiskas", "Whiskas");

    override fun toString(): String {
        return viewName
    }
}