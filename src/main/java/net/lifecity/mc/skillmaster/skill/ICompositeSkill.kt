package net.lifecity.mc.skillmaster.skill

interface ICompositeSkill : ISkill {
    val activationTime: Int
    val canCancel: Boolean

    fun onAdditionalInput()


}