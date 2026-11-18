import { describe, expect, it } from 'vitest'
import { decideAccess } from './access'

describe('路由访问控制', () => {
  it('未登录访问个人页面时跳转登录页并保留目标地址', () => {
    expect(decideAccess({ requiresAuth: true }, null, '/center')).toEqual({
      name: 'login', query: { redirect: '/center' },
    })
  })

  it('普通用户访问管理页面时跳转首页', () => {
    expect(decideAccess({ requiresAdmin: true }, { role: 'USER' }, '/admin')).toEqual({ name: 'home' })
  })

  it('管理员可以访问管理页面', () => {
    expect(decideAccess({ requiresAuth: true, requiresAdmin: true }, { role: 'ADMIN' }, '/admin')).toBe(true)
  })
})
