package utils

val specialCharacters: Map<Byte, String> = mapOf(
        0x00.toByte() to "¡",
        0x01.toByte() to "¿",
        0x02.toByte() to "Ä",
        0x03.toByte() to "À",
        0x04.toByte() to "Á",
        0x05.toByte() to "Â",
        0x06.toByte() to "Ã",
        0x07.toByte() to "Å",
        0x08.toByte() to "Ç",
        0x09.toByte() to "È",
        0x0A.toByte() to "É",
        0x0B.toByte() to "Ê",
        0x0C.toByte() to "Ë",
        0x0D.toByte() to "Ì",
        0x0E.toByte() to "Í",
        0x0F.toByte() to "Î",
        0x10.toByte() to "Ï",
        0x11.toByte() to "Ð",
        0x12.toByte() to "Ñ",
        0x13.toByte() to "Ò",
        0x14.toByte() to "Ó",
        0x15.toByte() to "Ô",
        0x16.toByte() to "Õ",
        0x17.toByte() to "Ö",
        0x18.toByte() to "Ø",
        0x19.toByte() to "Ù",
        0x1A.toByte() to "Ú",
        0x1B.toByte() to "Û",
        0x1C.toByte() to "Ü",
        0x1D.toByte() to "ß",
        0x1E.toByte() to "Þ",
        0x1F.toByte() to "à",
        // 0x20: Space
        // 0x21: !
        // 0x22: "
        0x23.toByte() to "á",
        0x24.toByte() to "â",
        // 0x25: %
        // 0x26: &
        // 0x27: "
        // 0x28: (
        // 0x29: )
        0x2A.toByte() to "~",
        0x2B.toByte() to  "♥",
        // 0x2C: ,
        // 0x90 is printed in place of 0x2D for debug msg #1 (-)
        // 0x2E: .
        0x2F.toByte() to "♪",

        // Skip to 0x3B

        0x3B.toByte() to "\uD83C\uDF22", // droplet 🌢
        // 0x3C - 0x40: unchanged

        // Skip to 0x5B
        0x5B.toByte() to "ã",
        0x5C.toByte() to "\uD83D\uDCA2", // anger 💢
        0x5D.toByte() to "ä",
        0x5E.toByte() to "å",
        // 0x5F: _
        0x60.toByte() to "ç",

        // Skip to 0x7B
        0x7B.toByte() to "è",
        0x7C.toByte() to "é",
        0x7D.toByte() to "ê",
        0x7E.toByte() to "ë",

        // Skip to 0x81
        0x81.toByte() to "ì",
        0x82.toByte() to "í",
        0x83.toByte() to "î",
        0x84.toByte() to "ï",
        0x85.toByte() to "·",
        0x86.toByte() to "ð",
        0x87.toByte() to "ñ",
        0x88.toByte() to "ò",
        0x89.toByte() to "ó",
        0x8A.toByte() to "ô",
        0x8B.toByte() to "õ",
        0x8C.toByte() to "ö",
        0x8D.toByte() to "ø",
        0x8E.toByte() to "ù",
        0x8F.toByte() to "ú",
        0x90.toByte() to "—",
        0x91.toByte() to "û",
        0x92.toByte() to "ü",
        0x93.toByte() to "ý",
        0x94.toByte() to "ÿ",
        0x95.toByte() to "þ",
        0x96.toByte() to "Ý",
        0x97.toByte() to "¦",
        0x98.toByte() to "§",
        0x99.toByte() to "ª",
        0x9A.toByte() to "º",
        0x9B.toByte() to "‖",
        0x9C.toByte() to "µ",
        0x9D.toByte() to "³",
        0x9E.toByte() to "²",
        0x9F.toByte() to "¹",
        0xA0.toByte() to "¯",
        0xA1.toByte() to "¬",
        0xA2.toByte() to "Æ",
        0xA3.toByte() to "æ",
        0xA4.toByte() to "‥",
        0xA5.toByte() to "»",
        0xA6.toByte() to "«",
        0xA7.toByte() to "☀",
        0xA8.toByte() to "☁",
        0xA9.toByte() to "☂",
        0xAA.toByte() to "\uD83C\uDF00", // cyclone 🌀
        0xAB.toByte() to "⛄",
        0xAC.toByte() to "⚞",
        0xAD.toByte() to "⚟",
        0xAE.toByte() to "／",
        0xAF.toByte() to "∞",
        0xB0.toByte() to "○",
        0xB1.toByte() to "╳",
        0xB2.toByte() to "□",
        0xB3.toByte() to "△",
        0xB4.toByte() to "＋",
        0xB5.toByte() to "⚡",
        0xB6.toByte() to "♂",
        0xB7.toByte() to "♁",
        0xB8.toByte() to "✿",
        0xB9.toByte() to "★",
        0xBA.toByte() to "\uD83D\uDC80", // skull 💀
        0xBB.toByte() to "\uD83D\uDE2E", // :O 😮
        0xBC.toByte() to "\uD83D\uDE0A", // ^_^ 😊 smiling mouth and eyes
        0xBD.toByte() to "\uD83D\uDE22", // sad :( 😢
        0xBE.toByte() to "\uD83D\uDE20", // angry >:( 😠
        0xBF.toByte() to "\uD83D\uDE03", // :D 😃
        0xC0.toByte() to "×",
        0xC1.toByte() to "÷",
        0xC2.toByte() to "\uD83D\uDD28", // mallet/hammer 🔨
        0xC3.toByte() to "\uD83C\uDF80", // ribbon 🎀
        0xC4.toByte() to "✉",
        0xC5.toByte() to "\uD83D\uDCB0", // bells bag 💰
        0xC6.toByte() to "\uD83D\uDC3E", // paw print 🐾
        0xC7.toByte() to "\uD83D\uDC36", // dog face 🐶
        0xC8.toByte() to "\uD83D\uDC31", // cat face 🐱
        0xC9.toByte() to "\uD83D\uDC30", // rabbit face 🐰
        0xCA.toByte() to "\uD83D\uDC14", // chicken face 🐔
        0xCB.toByte() to "\uD83D\uDC2E", // cow face 🐮
        0xCC.toByte() to "\uD83D\uDC37", // pig face 🐷
        0xCD.toByte() to "\n", // newline/carriage return; return symbol ⏎ in text
        0xCE.toByte() to "\uD83D\uDC1F", // fish 🐟
        0xCF.toByte() to "\uD83D\uDC1E", // bug/beetle 🐞
        0xD0.toByte() to ";",
        0xD1.toByte() to "#",
        // TODO These are placeholders for unknown blank spaces 0xD2 and 0xD3
        0xD2.toByte() to "░",
        0xD3.toByte() to "▓",
        0xD4.toByte() to "\uD83D\uDD11", // key 🔑
        0xD5.toByte() to "“", // left double quote
        0xD6.toByte() to "”", // right double quote
        0xD7.toByte() to "‘", // left single quote
        0xD8.toByte() to "’", // right single quote
        0xD9.toByte() to "Œ",
        0xDA.toByte() to "œ",
        // TODO These are placeholders for 0xDB - 0xDD French superscript ordinals e, re, er
        0xDB.toByte() to "☱",
        0xDC.toByte() to "☲",
        0xDD.toByte() to "☴",
        0xDE.toByte() to "\\"
)

val reverseSpecialCharacters: Map<String, Byte> = specialCharacters.entries.associateBy({ it.value }) { it.key }