package net.lifecity.mc.skillmaster

/**
 * 何らかのデータ型を武器に変換できなかった時に発生するエラー
 */
class WeaponConvertException(msg: String = "") : Exception(msg)

/**
 * 何らかのデータ型をスキルに変換できなかった時に発生するエラー
 */
class SkillConvertException(msg: String = "") : Exception(msg)

/**
 * SkillUserがSkillUserListの中に見つからなかった時に発生するエラー
 */
class SkillUserNotFoundException(msg: String = "") : Exception(msg)

/**
 * SkillがSkillSetの中に見つからなかった時に発生するエラー
 */
class SkillNotFoundException(msg: String = "") : Exception(msg)

